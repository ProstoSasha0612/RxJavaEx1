# RxJavaEx1 Task descriprion:
1) fun getUrlContent(urlList: List<String>): Single<List<String>> // parallel requests
2) fun isInternetOnline():  Observable<Boolean>
====
1) check internet online
2) get url content parallel requests
3) parallel with  get url content  check internet online each 1 second. and stop as soon as get url content complete
4) if internet offline throw error before get url content complete/throw error
