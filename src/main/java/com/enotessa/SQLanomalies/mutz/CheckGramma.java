package com.enotessa.SQLanomalies.mutz;

import org.python.util.PythonInterpreter;
import org.python.util.PythonObjectInputStream;

import java.util.*;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;

/**
 * Модель: Вывод структуры строки
 */
public class CheckGramma {
    ArrayList<ArrayList<String>> tokenizedSequences = new ArrayList<>();
    ArrayList<String> tokenizedSequence = new ArrayList<>();

    public void train(ArrayList<String> sequences) {
        for (String sequence : sequences){
            tokenizedSequence = tokenize(sequence);
            tokenizedSequences.add(tokenizedSequence);

            mergeSequence(tokenizedSequence, tokenizedSequences);
        }
    }

    public boolean validate(String sequence) {
        return true;
    }


    public ArrayList<String> tokenize(String sequence){
        tokenizedSequence = (ArrayList<String>) Arrays.asList(sequence.split(" "));
        return tokenizedSequence;
    }

    public void mergeSequence(ArrayList<String> tokenizedSequence, List<ArrayList<String>> dataset){
        // быстрый путь
        solutions = sequenceSolutions(tokenizedSequence);
    }

    public void sequenceSolutions(ArrayList<String> tokenizedSequence){

    }

    /*def merge_sequence(self, tokenized_sequence: TokensType,                                        # ==================================
                       dataset: typing.List[TokensType]):
            # Fast path
    solutions = self.sequence_solutions(tokenized_sequence)
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
            for prev_node_index, this_node_index, char in zip(
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
                        self.merge_node(node_a, node_b)*/







    public List sequenceSolutions(ArrayList<String> tokenizedSequence){
        int beam_size=100;
        /*Выполните поиск луча, чтобы эвристически найти наиболее вероятные пути.

        Полный перебор всех возможных путей часто вполне осуществим в окончательной модели.
        Однако при поиске лучшей модели можно попробовать модели с циклическими подструктурами:

            /- [A] -\\
        ^ -+   ↓ ↑  +--> $
            \\- [A] -/

        Это означает, что последовательность (например, AAAA) сгенерирует значительно увеличивающееся количество решений.
        Чтобы предотвратить это, используется эвристика поиска луча.

        Поиск луча поддерживает список наиболее вероятных найденных путей (количество путей называется «размером луча»)
        и продолжает только эти пути, даже если их может быть больше.

        Идея здесь в том, что если путь уже маловероятен,
        маловероятно (хотя и не невозможно, отсюда и эвристика), что путь станет вероятным.
*/
        PythonInterpreter pythonInterpreter = new PythonInterpreter();
        PythonObjectInputStream
        pythonInterpreter.exec();
        ArrayDeque q = new ArrayDeque();
        q.push(neg_log_p);
        /*q = queue.Queue()
        q.put(BeamSearchQueueItem(
                neg_log_p=0.0,
                node=self.root,
                path=[self.root.index]
        ))*/
        for (char ch : tokenizedSequence){

        }
        for next_char in tokenized_sequence:


    }

    class BeamSearchQueueItem{

        BeamSearchQueueItem (float negLogP, node, ArrayList<Integer> path){

        }
    }

    /*def sequence_solutions(self, tokenized_sequence: TokensType,
                           beam_size: int=100) \
            -> typing.List[SequenceSolution]:
    q = queue.Queue()
            q.put(BeamSearchQueueItem(
            neg_log_p=0.0,
            node=self.root,
            path=[self.root.index]
    ))

            for next_char in tokenized_sequence:
            # Prepear a PriorityQueue, from this the `beam_size` most likely
            # paths will be extracted and put into the queue `q` in the `char`
            # iteration.
            next_q = queue.PriorityQueue()

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
            for _ in range(min(beam_size, next_q.qsize())):
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

            return solutions*/
}

