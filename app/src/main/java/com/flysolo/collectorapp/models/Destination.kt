package com.flysolo.collectorapp.models

class Destination(var destinationID : String? = null,
                  var collectorID : String? = null,
                  var listAddresses: List<String>? = null,
                  var isNowCollecting : Int? = 0,
                  var timestamp : Long? = null)
