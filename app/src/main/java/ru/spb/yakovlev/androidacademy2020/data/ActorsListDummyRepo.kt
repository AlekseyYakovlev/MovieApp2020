package ru.spb.yakovlev.androidacademy2020.data

import ru.spb.yakovlev.androidacademy2020.model.ActorData
import ru.spb.yakovlev.androidacademy2020.model.ActorItemData
import ru.spb.yakovlev.androidacademy2020.model.toActorItemData

object ActorsListDummyRepo {
    val actorsList = listOf<ActorData>(
        ActorData(
            id = 1,
            name = "Robert Downey Jr.",
            photo = "https://s3-alpha-sig.figma.com/img/a58c/56ed/0f8f5ec204f1a37e0e15e2a731ab190a?Expires=1607904000&Signature=SWeOjEBPXMvk4AgllPX26koPnfC1qObkKvh~swrpTmJ66DqTi89HUrdgZEc3zZIc2SmBCSmhv2c-EXFYe6~imSUyQ4UUFcFXpepEa-gjOiRkT8HHw2b13UyB3SshNkr~Hg05Gpc~-UzQlrXmq-m97wlCOYNNmR~coUzRTPBfrFcGS2~xj89nzef7~0OijbLjhcSpUK4bS0wRkopW-LIlVVIsKrq1FpbkF-G1k6cLurPm6QMoQUWcCWzbV0VmThpBfVq-fokrGZcaVc3pd3HcscJz4e0RBK8VXM7zO9u8eA~DjMt5Cl8KnXhdY3WsINbz28zPQUSDUg9mfdDGNP~PUw__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA",
        ),
        ActorData(
            id = 2,
            name = "Chris Evans",
            photo = "https://s3-alpha-sig.figma.com/img/95e5/6794/5c9007d7ca26a2d00d93fe8d18c89643?Expires=1607904000&Signature=BEDvd3lKRFOPgJWFm~RZ7F3yC9RSBvOH28-xYLy3fVGZROOt6ZZkO8lJ5caZirEIzrckNZxYSjqs~40fsWNCpav36tdRKjQJlcMxV4ko8XQ44hk6FNfRlb0gqnggzWqZ-vpGKU1cwDwFYKGKQdLH1w6V98jPKrPATN684NZdyJWYe7fnz2AdOsqVhLJ6G0vOVDzfxyjUyl59YQM2B1Sw11ySkgUc-q1mN52MidGbdO~GxYRVx9eZjwOaFfsbXbfeGzJRrjReffJmRWjt3-Dq3oTtxUy3949mzyoLH3OFU6JZVB3yOv3Z8q4h-yL2o95aB9IECHrTd00h34wnBRGEBQ__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA",
        ),
        ActorData(
            id = 3,
            name = "Mark Ruffalo",
            photo = "https://s3-alpha-sig.figma.com/img/d3fa/bc44/eeb8d6e5b9c059a681dcbe67501dceae?Expires=1607904000&Signature=PqpCICXzpUfvJu7rN2zY8kLo-qO4c57Ngaf50Td18g-arwP8lKscrrnjyzHCeS9b3FkZWcxMe8jOXHbcy2r~Y4LYwc2bJE~HVvLRG3hEtrV3gqVWCp-ltXvHMRkRG3g0MzDtmxJZOHZYuUgJSCzb-Q5PsnDTiRJ2F5uYOz~l5UBD4gDiBSdGZAZ6uV3agim50YnJi4IKLXJ3NWeIHGoP-EpCFZUUqY8PL5visietEul5AOGBcuWiv4TzrUvclEQCgDxP5nA-cXRBBAZRvmloB1dKH3-pkrPyx6i7gluf8WNb0HyY8-x41wGfP6G1uNKItGSFwqubgfHYO1Nhh1cW5g__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA",
        ),
        ActorData(
            id = 4,
            name = "Chris Hemsworth",
            photo = "https://s3-alpha-sig.figma.com/img/7bd1/c6fe/f56e2ec650a0ee6e64c9b1356e383bb3?Expires=1607904000&Signature=Hh-rvOX3OpNKmFNFM8Rp~dL42EaupGdIb54jPo6IraIW7MqXQl5vMN7BSLrv-IkzCu4P7wnwYiwC6rxOa2UNExwPiKkiaotvun7oY1ZuEOKRMImvfrUfnEgE4~Ml~o3HtZeiC8wiz34CDdBFQzb5Z9LTZsemLEPZv3eDKpSurSSvzDNvwgOV5QJZ4IikdtC0ZJdxZOlJvG1Xlc304XOMazDFB4YXKkTOi4iwX5UjxrRZyl1I7y-sYeHSfC0dYTIkj3YMY6IGuX32H454troDg0VVnHjezCV3k9Njm7gm4KWA~6wEG3CTMuMK4ZtciSagSL2S70X5p0JytoF6V8-lZA__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA",
        ),
        ActorData(
            id = 5,
            name = "Robert Downey Jr.",
            photo = "https://s3-alpha-sig.figma.com/img/a58c/56ed/0f8f5ec204f1a37e0e15e2a731ab190a?Expires=1607904000&Signature=SWeOjEBPXMvk4AgllPX26koPnfC1qObkKvh~swrpTmJ66DqTi89HUrdgZEc3zZIc2SmBCSmhv2c-EXFYe6~imSUyQ4UUFcFXpepEa-gjOiRkT8HHw2b13UyB3SshNkr~Hg05Gpc~-UzQlrXmq-m97wlCOYNNmR~coUzRTPBfrFcGS2~xj89nzef7~0OijbLjhcSpUK4bS0wRkopW-LIlVVIsKrq1FpbkF-G1k6cLurPm6QMoQUWcCWzbV0VmThpBfVq-fokrGZcaVc3pd3HcscJz4e0RBK8VXM7zO9u8eA~DjMt5Cl8KnXhdY3WsINbz28zPQUSDUg9mfdDGNP~PUw__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA",
        ),
        ActorData(
            id = 6,
            name = "Chris Evans",
            photo = "https://s3-alpha-sig.figma.com/img/95e5/6794/5c9007d7ca26a2d00d93fe8d18c89643?Expires=1607904000&Signature=BEDvd3lKRFOPgJWFm~RZ7F3yC9RSBvOH28-xYLy3fVGZROOt6ZZkO8lJ5caZirEIzrckNZxYSjqs~40fsWNCpav36tdRKjQJlcMxV4ko8XQ44hk6FNfRlb0gqnggzWqZ-vpGKU1cwDwFYKGKQdLH1w6V98jPKrPATN684NZdyJWYe7fnz2AdOsqVhLJ6G0vOVDzfxyjUyl59YQM2B1Sw11ySkgUc-q1mN52MidGbdO~GxYRVx9eZjwOaFfsbXbfeGzJRrjReffJmRWjt3-Dq3oTtxUy3949mzyoLH3OFU6JZVB3yOv3Z8q4h-yL2o95aB9IECHrTd00h34wnBRGEBQ__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA",
        ),
        ActorData(
            id = 7,
            name = "Mark Ruffalo",
            photo = "https://s3-alpha-sig.figma.com/img/d3fa/bc44/eeb8d6e5b9c059a681dcbe67501dceae?Expires=1607904000&Signature=PqpCICXzpUfvJu7rN2zY8kLo-qO4c57Ngaf50Td18g-arwP8lKscrrnjyzHCeS9b3FkZWcxMe8jOXHbcy2r~Y4LYwc2bJE~HVvLRG3hEtrV3gqVWCp-ltXvHMRkRG3g0MzDtmxJZOHZYuUgJSCzb-Q5PsnDTiRJ2F5uYOz~l5UBD4gDiBSdGZAZ6uV3agim50YnJi4IKLXJ3NWeIHGoP-EpCFZUUqY8PL5visietEul5AOGBcuWiv4TzrUvclEQCgDxP5nA-cXRBBAZRvmloB1dKH3-pkrPyx6i7gluf8WNb0HyY8-x41wGfP6G1uNKItGSFwqubgfHYO1Nhh1cW5g__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA",
        ),
        ActorData(
            id = 8,
            name = "Chris Hemsworth",
            photo = "https://s3-alpha-sig.figma.com/img/7bd1/c6fe/f56e2ec650a0ee6e64c9b1356e383bb3?Expires=1607904000&Signature=Hh-rvOX3OpNKmFNFM8Rp~dL42EaupGdIb54jPo6IraIW7MqXQl5vMN7BSLrv-IkzCu4P7wnwYiwC6rxOa2UNExwPiKkiaotvun7oY1ZuEOKRMImvfrUfnEgE4~Ml~o3HtZeiC8wiz34CDdBFQzb5Z9LTZsemLEPZv3eDKpSurSSvzDNvwgOV5QJZ4IikdtC0ZJdxZOlJvG1Xlc304XOMazDFB4YXKkTOi4iwX5UjxrRZyl1I7y-sYeHSfC0dYTIkj3YMY6IGuX32H454troDg0VVnHjezCV3k9Njm7gm4KWA~6wEG3CTMuMK4ZtciSagSL2S70X5p0JytoF6V8-lZA__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA",
        ),
    )

    val actorItemList: List<ActorItemData> = actorsList.map { it.toActorItemData() }

    fun getActorItemsById(ids: List<Int>) = actorItemList.filter { it.id in ids }
}