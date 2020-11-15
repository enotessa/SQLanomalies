package com.enotessa.SQLanomalies.mutz;
/*Длина строки
Проверяет, является ли длина маловероятной.
Учитывая, что распределение длины последовательности неизвестно и
вряд ли можно сделать какие-либо разумные предположения о нем
, вместо этого используется неравенство Чебышева.
Неравенство Чебышева говорит, что вероятность того, что что-то (x)
отклоняется от среднего значения больше, чем порог (t), меньше, чем `\sigma^2 / t^2`
                    p(|x - \mu| > t) < \sigma^2 / t^2
Это переформулируется в "вероятность того, что что-то (x)
отклоняется больше от среднего, чем текущее отклонение (|l - \mu|). Where `l`
это текущая длина строки ресурса.
              p(|x - \mu| > |l - \mu|) < \sigma^2 / (l - \mu)^2*/

public class CheckLength {

}

/*
import numpy as np




class CheckLengthModel(typing.NamedTuple):
        mean: float
        var: float


        def train(sequences: typing.Iterable[str], verbose: bool=False) \
        -> CheckLengthModel:
        if verbose:
        print(f'Training LengthModel with {len(sequences)} sequences')

        lengths = np.fromiter(map(len, sequences), int)

        return CheckLengthModel(
        mean=np.mean(lengths),
        var=np.var(lengths)
        )


        def validate(model: CheckLengthModel, sequence: str,
        threshold: float=0.1) -> bool:
        upper_bound = model.var / (len(sequence) - model.mean) ** 2

        return upper_bound >= threshold*/
