define([], function(){
	    var link = document.createElement("link");
	    link.type = "text/css";
	    link.rel = "stylesheet";
	    link.href = '//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css';
	    document.getElementsByTagName("head")[0].appendChild(link);
	    
	    link = document.createElement("link");
      link.type = "text/css";
      link.rel = "stylesheet";
      link.href = '/container/style/notific8.min.css';
      document.getElementsByTagName("head")[0].appendChild(link);

	    link = document.createElement("link");
	    link.type = "text/css";
	    link.rel = "stylesheet";
	    link.href = '/container/style/mev.css';
	    document.getElementsByTagName("head")[0].appendChild(link);
});