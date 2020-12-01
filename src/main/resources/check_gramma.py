# -*- coding: utf-8 -*-
from __future__ import division
from __future__ import absolute_import
import copy
import math
import Queue
import itertools
import functools
from itertools import imap
from itertools import izip
from collections import namedtuple
from collections import defaultdict
from operator import itemgetter
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

collaps_chars = dict()
collaps_chars.update(dict((char, u'<0-9>') for char in u'0123456789'))
collaps_chars.update(dict((char, u'<a-f>') for char in u'abcdef'))
collaps_chars.update(dict((char, u'<A-F>') for char in u'ABCDEF'))
collaps_chars.update(dict((char, u'<g-z>') for char in u'ghijklmnopqurstuvwxyz'))
collaps_chars.update(dict((char, u'<G-Z>') for char in u'GHIJKLMNOPQURSTUVWXYZ'))
collaps_chars.update(dict((char, u'<'+u'\\u0430'.encode('UTF-8')+u'-'+u'\u044f'.encode('UTF-8')+u'>') for char in u'\u0430\u0431\u0432\u0433\u0434\u0435\u0451\u0436\u0437\u0438\u0439\u043a\u043b\u043c\u043d\u043e\u043f\u0440\u0441\u0442\u0443\u0444\u0445\u0446\u0447\u0448\u0449\u044c\u044b\u044a\u044d\u044e\u044f'))
collaps_chars.update(dict((char, u'<'+u'\\u0410'.encode('cp1251')+u'-'+u'\u042f'.encode('UTF-8')+u'>') for char in u'\u0410\u0411\u0412\u0413\u0414\u0415\u0401\u0416\u0417\u0418\u0419\u041a\u041b\u041c\u041d\u041e\u041f\u0420\u0421\u0422\u0423\u0424\u0425\u0426\u0427\u0428\u0429\u042c\u042b\u042a\u042d\u042e\u042f'))


#GrammaNodeType = typing.TypeVar(u'GrammaNode', bound=u'GrammaNode')
#KeyType = typing.TypeVar(u'Key')
#ValueType = typing.TypeVar(u'Value')
#TokensType = typing.List[unicode]


def tokenize(sequence):                                                      # -------------------------------------------------
    return [
        collaps_chars[char] if char in collaps_chars else char
        for char in sequence
    ]


class SparseDefaultDict(defaultdict):
    # The typing.DefaultDict will create an item in the dict when it is read,
    # even if the item is never written to. To prevent this, create a subtype
    # with a __missing__ handler.
    def __missing__(self, key):
        return self.default_factory()


#class SequenceSolution(object):
#    log_properbility=0
#    path=0

SequenceSolution = namedtuple('SequenceSolution', ['log_properbility', 'path'])




BeamSearchQueueItem = namedtuple('BeamSearchQueueItem', ['neg_log_p', 'node', 'path'])



#class BeamSearchQueueItem(object):
##
##
#   def __init__(self, neg_log_p, node, path):
#       self.neg_log_p=neg_log_p
#      self.node=node
#      self.path=path
#
#   def __eq__(self, other):
#      if not isinstance(other, BeamSearchQueueItem):
#         return NotImplemented
#
#       return self.neg_log_p == other.neg_log_p
#
#   def __lt__(self, other):
#      if not isinstance(other, BeamSearchQueueItem):
#         return NotImplemented
#
#       return self.neg_log_p < other.neg_log_p


BeamSearchQueueItem = functools.total_ordering(BeamSearchQueueItem)

class GrammaNode(object):
    index=0
    is_root=True
    is_end=True
    allow_merge=True
    emission=object
    num_aggregated_emissions=0
    transition=object
    num_aggregated_transitions=0
    parents=object

    def __init__(self, index, is_root=False, is_end=False):
        self.index = index
        self.is_root = is_root
        self.is_end = is_end
        self.allow_merge = False
        self.emission = SparseDefaultDict(float)
        self.num_aggregated_emissions = 0
        self.transition = SparseDefaultDict(float)
        self.num_aggregated_transitions = 0
        self.parents = set()

    def stringify_emission(self):
        if self.is_root:
            return u'^'
        elif self.is_end:
            return u'$'
        else:
            return u' | '.join([
                u'{char} = {properbility}'
                for char, properbility in self.emission.items()
            ])

    def stringify_transition(self):
        return u', '.join([
            u'{properbility} -> {index}'
            for index, properbility in self.transition.items()
        ])

    def stringify(self):
        return (u'{self.index}: ({self.stringify_emission()}) ==> '
                u'{{{self.stringify_transition()}}}')

    def increment_emission(self, inc_char):
        u"""
        Increase the properbility of emission `inc_char`.

        The properbility is increased by an amount corresponding to there
        being one more observation containing this emission at this state in
        the markov chain.
        """

        n = self.num_aggregated_emissions
        rescale = n / (n + 1)

        # rescale all existing emissions to make room for the incremented char
        for char, properbility in self.emission.items():
            self.emission[char] = rescale * properbility

        # Insert or increment the `inc_char`
        self.emission[inc_char] += 1 / (n + 1)
        self.num_aggregated_emissions = n + 1

    def increment_transition(self, inc_destination):
        u"""
        Increase the properbility of transition `inc_destination`.

        The properbility is increased by an amount corresponding to there
        being one more observation containing this transition at this state in
        the markov chain.
        """
        inc_index = inc_destination.index
        n = self.num_aggregated_transitions
        rescale = n / (n + 1)

        # rescale all existing emissions to make room for the incremented char
        for index, properbility in self.transition.items():
            self.transition[index] = rescale * properbility

        # Insert or increment the `inc_char`
        self.transition[inc_index] += 1 / (n + 1)
        self.num_aggregated_transitions = n + 1

        # Add this node as parent to the description
        inc_destination.parents.add(self.index)

    def merge_emissions(self, node):
        u"""
        Merge the emissions from `node` into this node.

        The emissions from `node` are not changed, only this object is changed.
        """

        # Merge emissions
        total_aggregated_emissions = (self.num_aggregated_emissions +
                                      node.num_aggregated_emissions)
        self_emission_rescale = (self.num_aggregated_emissions /
                                 total_aggregated_emissions)
        node_emission_rescale = (node.num_aggregated_emissions /
                                 total_aggregated_emissions)
        #WARN HERE
        #for char in (self.emission.keys() | node.emission.keys()):
        for char in (set(self.emission.keys()) | set(node.emission.keys())):
            self.emission[char] = (
                    self_emission_rescale * self.emission[char] +
                    node_emission_rescale * node.emission[char]
            )

        self.num_aggregated_emissions = total_aggregated_emissions

    def merge_transitions(self, node):
        u"""
        Merge the transitions from `node` into this node.

        The transitions from `node` are not changed, only this object is
        changed.
        """
        # Merge outgoing transitions
        total_aggregated_transitions = (self.num_aggregated_transitions +
                                        node.num_aggregated_transitions)
        self_transition_rescale = (self.num_aggregated_transitions /
                                   total_aggregated_transitions)
        node_transition_rescale = (node.num_aggregated_transitions /
                                   total_aggregated_transitions)

        # Exclude transitions that will become a self-recursive transition
        # when merged. They need to be treated specially.
        print u'WARN:  self.transition:{}, node.transition:{}, self.index:{}, node.index:{}'.format(self.transition.keys(),node.transition.keys(),self.index,node.index)

        non_self_recursive_transitions = (
            #WARN HERE
                ( set(self.transition.keys()) | set(node.transition.keys())) -
                set([self.index, node.index])
        )
        print u'non_self_recursive_transitions:{}'.format(non_self_recursive_transitions)
        print u'merge_transitions 1: node.transition:{}, node.index:{}'.format(node.transition.keys(),node.index)

        for index in non_self_recursive_transitions:
            self.transition[index] = (
                    #self_transition_rescale * self.transition[index] +
                    self_transition_rescale * itemgetter(index)(copy.copy(self.transition)) +
                    #node_transition_rescale * node.transition[index]
                    node_transition_rescale * itemgetter(index)(copy.copy(node.transition))
            )
        print u'merge_transitions 2: node.transition:{}, node.index:{}'.format(node.transition.keys(),node.index)

        # For the self-recursive transition, there are a few more links to
        # consider when merging.
        #self_recursive_link=0.0

        #JYTHON BUG HERE!!!!!!!
        #stsi =copy.copy(self.transition[self.index])
        stsi =itemgetter(self.index)(copy.copy(self.transition))
        #stni = copy.copy(self.transition[node.index])
        stni = itemgetter(node.index)(copy.copy(self.transition))
        print u'ntni before {}'.format(node.transition.keys())
        #ntni = copy.copy(node.transition[node.index])
        ntni = itemgetter(node.index)(copy.copy(node.transition))
        print u'ntni after {}'.format(node.transition.keys())

        #ntsi = copy.deepcopy(node.transition[self.index])
        ntsi = itemgetter(self.index)(copy.copy(node.transition))

        self_recursive_link = (
            # The self-recursive link in `self`
                ##self_transition_rescale * self.transition[self.index] +
                self_transition_rescale * stsi +
                # Link from `self` to `node` becomes self-recursive
                ##self_transition_rescale * self.transition[node.index] +
                self_transition_rescale * stni +
                # The self-recursive link in `node` is transfered to `self`
                ##node_transition_rescale * node.transition[node.index] +
                node_transition_rescale * ntni +
                # Link from `node` to `self` becomes self-recursive
                ##node_transition_rescale * node.transition[self.index]
                node_transition_rescale * ntsi
        )
        print u'merge_transitions 3: node.transition:{}, node.index:{}'.format(node.transition.keys(),node.index)

        # Only set the self-recursive link, if a self-recursive link was
        # produced

        print u'self_recursive_link:{}'.format(self_recursive_link)
        if self_recursive_link > 0.0:
            self.transition[self.index] = self_recursive_link
        # Also remove transition to the `node` as this has now become
        # a self-recursive link
        print u'merge_transitions 4: node.transition:{}, node.index:{}'.format(node.transition.keys(),node.index)

        if node.index in self.transition:
            del self.transition[node.index]
        print u'merge_transitions 5: node.transition:{}, node.index:{}'.format(node.transition.keys(),node.index)

        self.num_aggregated_transitions = total_aggregated_transitions
        print u'merge_transitions 6: node.transition:{}, node.index:{}'.format(node.transition.keys(),node.index)

        print u'total_aggregated_transitions:{}'.format(total_aggregated_transitions)

class CheckGrammaModel(object):
    #root=object
    #end=object
    #nodes=object
    #index_counter=0

    def __init__(self):
        self.root = GrammaNode(0, is_root=True)
        self.end = GrammaNode(1, is_end=True)
        self.nodes = dict()
        self.nodes[self.root.index] = self.root
        self.nodes[self.end.index] = self.end
        self.index_counter = 2

    def copy(self):
        return copy.deepcopy(self)

    def stringify(self):
        output = u''
        for node in self.nodes.values():
            output += node.stringify() + u'\n'
        return output

    def compute_prior_log_prop(self):

        connections = sum(
            3 * (len(node.emission) - 1) + 1 + len(node.transition)
            for node in self.nodes.values()
            if not node.is_root and not node.is_end
        )
        connections += len(self.root.transition)

        return - math.log(len(self.nodes)) * connections

    def sequence_solutions(self, tokenized_sequence,                # ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                           beam_size=100):
        q = Queue.Queue()
        q.put(BeamSearchQueueItem(
            neg_log_p=0.0,
            node=self.root,
            path=[self.root.index]
        ))

        for next_char in tokenized_sequence:
            # Prepear a PriorityQueue, from this the `beam_size` most likely
            # paths will be extracted and put into the queue `q` in the `char`
            # iteration.
            next_q = Queue.PriorityQueue()

            while not q.empty():
                (neg_log_p, node, path) = q.get()

                # Check each transition for the `node` and if the transition
                # points to a node that can emit `next_char` add the transition
                # to the `next_q`.
                for transition, transition_p in node.transition.items():
                    next_node = self.nodes[transition]

                    if next_char in next_node.emission:
                        emission_p = next_node.emission[next_char]
                        next_q.put(BeamSearchQueueItem(
                            neg_log_p=neg_log_p - math.log(transition_p)
                                      - math.log(emission_p),
                            node=next_node,
                            path=path + [next_node.index]
                        ))

            # Transfer the most likely paths from `next_q` to the `q`.
            for _ in xrange(min(beam_size, next_q.qsize())):
                q.put(next_q.get())

        # The end of the tokenized_sequence have been reached, now the
        # transitions must point to the `end` node.
        end_index = self.end.index

        solutions = []
        while not q.empty():
            (neg_log_p, node, path) = q.get()

            # If the end node is a transition in `node`, the `path` is a valid
            # solution.
            if end_index in node.transition:
                transition_p = node.transition[end_index]
                solutions.append(SequenceSolution(
                    log_properbility=-neg_log_p + math.log(transition_p),
                    path=path + [end_index]
                ))

        return solutions

    def sequence_properbility(self, tokenized_sequence):
        u"""
        Compute the sequence properbility.

        This differes from compute_sequence_log_prop in that it return
        P(D|M) = 0, if no solutions to the sequence where found. This is
        used for validating a sequence.
        """
        solutions = self.sequence_solutions(tokenized_sequence)

        if len(solutions) == 0:
            return 0

        # P(D|M) = sum_{p \in paths} P(D|M,p)
        return sum(imap(
            lambda solution: math.exp(solution.log_properbility),
            solutions
        ))

    def compute_sequence_log_prop(self, tokenized_sequence):
        u"""
        Compute the sequence log properbility.

        Compute the log properbility of the sequence. This implementation
        is used in the cost computation and a solution must therefore exist.

        It also contains a fast path, for when there is only one solution.
        """
        solutions = self.sequence_solutions(tokenized_sequence)

        if len(solutions) == 0:
            # In theory there should always be a solution. However, because
            # the solution finder uses a Beam Search the valid solution may
            # not be found.
            # If that is the case, the graph structure is bad anyway. So
            # return log(0) = -infinity
            return -float(u'inf')

        if len(solutions) == 1:
            return solutions[0].log_properbility

        # P(D|M) = sum_{p \in paths} P(D|M,p)
        # log(P(D|M)) = log( sum_{p \in paths} exp(log(P(D|M,p))) )
        return math.log(sum(imap(
            lambda solution: math.exp(solution.log_properbility),
            solutions
        )))

    def compute_cost(self, dataset):
        u"""
        Computes the model cost, given a dataset.

        The cost is defined as the negative log-properbility of P(M|D).
        """
        # unnormalized_posterior = p(data) * p(prior)
        # log(unnormalized_posterior) = log(p(data)) + log(p(prior))
        log_prior = self.compute_prior_log_prop()
        log_data = sum(
            imap(self.compute_sequence_log_prop, dataset)
        )
        unnormalized_log_posterior = log_data + log_prior
        return -unnormalized_log_posterior

    def add_node(self):
        new_node = GrammaNode(self.index_counter)
        self.index_counter += 1
        self.nodes[new_node.index] = new_node
        return new_node

    def merge_node(self, keep_node, remove_node):
        print u'before merge remove_node.transition.keys:{}, node index:{}'.format(remove_node.transition.keys(),remove_node.index)
        keep_node.merge_emissions(remove_node)
        print u'after merge emissions remove_node.transition.keys:{}, node index:{}'.format(remove_node.transition.keys(),remove_node.index)
        keep_node.merge_transitions(remove_node)
        print u'after merge transitions remove_node.transition.keys:{}, node index:{}'.format(remove_node.transition.keys(),remove_node.index)

        # Merge the ingoing transitions
        for remove_parent_index in remove_node.parents:
            # Skip the self-recursive link
            if remove_parent_index == keep_node.index:
                continue

            parent_node = self.nodes[remove_parent_index]

            # Transfer the properbility (flux) from the `remove_node` to the
            # `keep_node`.
            parent_node.transition[keep_node.index] += \
                parent_node.transition[remove_node.index]
            del parent_node.transition[remove_node.index]

            # Now that the parent_node from the remove_node is a parent of
            # keep_node, update the parents set.
            keep_node.parents.add(remove_parent_index)

        # Redirect the `parent` of the removed node children.
        print u'remove_node.transition.keys:{}, node index:{}'.format(remove_node.transition.keys(),remove_node.index)

        for child_index in remove_node.transition.keys():
            # Skip the self-recursive link
            if child_index == remove_node.index:
                continue
            child_node = self.nodes[child_index]
            print u'child_index in remove_node  child_index:{}: remove_node.index:{} child_node.par:{}'.format(child_index, remove_node.index, child_node.parents)
            child_node.parents.remove(remove_node.index)
            child_node.parents.add(keep_node.index)

        # Finally remove the node from the graph table
        del self.nodes[remove_node.index]

        # The node is no longer in the Graph, so it can no longer be merged
        remove_node.allow_merge = False

    def compute_merge_cost(self,
                           keep_node,
                           remove_node,
                           dataset):
        # Create a copy of the current model, such that the cost of
        # a potential merge can be computed without interfering the main
        # model.
        suggested_model = self.copy()
        suggested_model.merge_node(
            suggested_model.nodes[keep_node.index],
            suggested_model.nodes[remove_node.index]
        )
        return suggested_model.compute_cost(dataset)

    def add_unmerged_sequence(self, tokenized_sequence):
        unmerged_nodes = []

        # Build a path between root and end node, that contains the new
        # sequence.
        previuse_node = self.root
        previuse_char = None
        for char in tokenized_sequence:
            # It always pays off on the baysian prior to merge subsequent nodes
            # with the same emission. The only penalty is on posterior
            # properbility is P(X|M), however the penalty here is usually
            # very small. Because this dramatically decrease the number of
            # nodes, improves generalization, and is almost always correct
            # identical subsequent nodes are merged, without computing the
            # posterior properbility:
            #
            #   ^ -> A -> A -> A -> / -> B -> B -> B -> $
            #
            # Should be merged to:
            #
            #   ^ ->  [A]  -> / ->  [B]  -> $
            if char == previuse_char:
                # Merge if emission is identical
                previuse_node.increment_transition(previuse_node)
                previuse_node.increment_emission(char)
            else:
                # Create new node if emission is *not* identical
                new_node = self.add_node()
                unmerged_nodes.append(new_node)
                new_node.increment_emission(char)
                previuse_node.increment_transition(new_node)
                previuse_node = new_node
        previuse_node.increment_transition(self.end)

        return unmerged_nodes

    def find_optimal_merge(self, node,
                           dataset):
        best_model_cost = self.compute_cost(dataset)
        best_merge_node = None

        for suggested_merge_node in self.nodes.values():
            # Nodes that doesn't allow for merge are either the root or end
            # node, or a node futher down the unmerged sequence that is
            # currently being merged.
            if not suggested_merge_node.allow_merge:
                continue

            print u'compute_merge_cost  {}: dataset:{} node:{}'.format(suggested_merge_node.index, dataset, node.index)
            print u'compute_merge_cost2 node.transition {}'.format(node.transition.keys())
            suggested_model_cost = self.compute_merge_cost(
                suggested_merge_node, node, dataset
            )

            if suggested_model_cost < best_model_cost:
                best_model_cost = suggested_model_cost
                best_merge_node = suggested_merge_node

        return best_merge_node

    def merge_sequence(self, tokenized_sequence,                                        # ==================================
                       dataset):

        print u'merge_sequence  {}: {}'.format(tokenized_sequence, dataset)
        # Fast path
        solutions = self.sequence_solutions(tokenized_sequence)
        print u'solutions  : {}'.format(len(solutions))

        if len(solutions) > 0:
            # If tokenized_sequence is allready representable by the network.
            # If so, a more complex network won't benfit because of P(M) and
            # the focus should be on P(D|M) that can likely be optimized by
            # just increment the highest properbility path.
            best_solution = solutions[0]
            for solution in solutions[1:]:
                if solution.log_properbility > best_solution.log_properbility:
                    best_solution = solution

            # increment transition and emission along the path
            for prev_node_index, this_node_index, char in izip(
                    best_solution.path[:-2],
                    best_solution.path[1:-1],
                    tokenized_sequence
            ):
                prev_node = self.nodes[prev_node_index]
                this_node = self.nodes[this_node_index]

                prev_node.increment_transition(this_node)
                this_node.increment_emission(char)

            # In the above the end node is excluded, so increment the
            # transition between the last real node `this_node` and the end
            # node.
            self.nodes[best_solution.path[-2]].increment_transition(self.end)

        else:
            # Slow path
            unmerged_nodes = self.add_unmerged_sequence(tokenized_sequence)
            print u'unmerged_nodes  : {}'.format(len(unmerged_nodes))
            merge_nodes = set()
            for node in unmerged_nodes:
                best_merge_node = self.find_optimal_merge(node, dataset)
                if best_merge_node is None:
                    # If there are no optimial merges, then mark this node
                    # as done. This allows futures unmerged nodes to merge into
                    # this node.
                    node.allow_merge = True
                    merge_nodes.add(node)
                else:
                    # Found an optimal merge, execute merge on the master model
                    # and remove the new node from the list.
                    self.merge_node(best_merge_node, node)
                    merge_nodes.add(best_merge_node)

            # Purne 1-cycles among the merged nodes
            model_cost = self.compute_cost(dataset)
            for node_a, node_b in itertools.combinations(merge_nodes, 2):
                # If the two nodes links to themself and each other and
                # a merge is allowed. Try merging them together:
                if (
                        node_a.allow_merge and
                        node_b.allow_merge and
                        node_a.index in node_a.transition and
                        node_a.index in node_a.transition and
                        node_b.index in node_b.transition and
                        node_a.index in node_b.transition
                ):
                    suggested_model_cost = self.compute_merge_cost(
                        node_a, node_b, dataset
                    )
                    # If the merge improves the model cost, update the model
                    if suggested_model_cost < model_cost:
                        model_cost = suggested_model_cost
                        self.merge_node(node_a, node_b)


def train(sequences, verbose=False):
    if verbose:
        #print 'Training GrammaModel with {len(sequences)} sequences'.format(len(sequences)
        print u'Training GrammaModel with  {}: {}'.format(len(sequences), sequences)
    model = CheckGrammaModel()

    # Add remaining sequences by merge
    tokenized_sequences = []
    for i, sequence in enumerate(sequences):
        if verbose:
            #print u'  {i}: {sequence}'
            print u'  {}: {}'.format(i, sequence)
        tokenized_sequence = tokenize(sequence)
        tokenized_sequences.append(tokenized_sequence)
        model.merge_sequence(tokenized_sequence, tokenized_sequences)

    return model


def validate(model, sequence,
             threshold=0.0):
    dsf = tokenize(sequence)
    print dsf
    return model.sequence_properbility(dsf) > threshold
