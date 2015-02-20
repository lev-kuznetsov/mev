suite ("Common clustering API", function () 
  suite ("Dissimilatiry matrix calculation", function (data = data.frame (first = c (.1, .2),
                                                                          second = c (.3, .4)),
                                                       methods = list (euclidean = .1414,
                                                                       canberra = .4762,
                                                                       maximum = .1,
                                                                       manhattan = .2,
                                                                       binary = .0)) inject (function (dist) {
    it ("Should define dissimilatiry matrix injectable", function ()
      expect (is.function (dist)));
    
    for (distance in names (methods))
      it (paste ("Should calculate using", distance, "metric"), function ()
        expect (abs (methods[[ distance ]] - dist (data, list (type = distance))) < .0001));
  })));
