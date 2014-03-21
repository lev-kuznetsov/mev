define([], function(){
	    var link = document.createElement("link");
	    link.type = "text/css";
	    link.rel = "stylesheet";
	    link.href = '//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css';
	    document.getElementsByTagName("head")[0].appendChild(link);
	    
	    link = document.createElement("link");
	    link.type = "text/css";
	    link.rel = "stylesheet";
	    link.href = '//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css';
	    document.getElementsByTagName("head")[0].appendChild(link);
	    
	    link = document.createElement("link");
	    link.type = "text/css";
	    link.rel = "stylesheet";
	    link.href = '/container/style/mev.css';
	    document.getElementsByTagName("head")[0].appendChild(link);
	    
	    link = document.createElement("link");
        link.type = "text/css";
        link.rel = "stylesheet";
        link.href = '/container/style/notific8.min.css';
        document.getElementsByTagName("head")[0].appendChild(link);
});