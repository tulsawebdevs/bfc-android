Zepto(function($){
  
  var NavBar = $(".nav");
  
  var PhotoModel = Backbone.Model.extend({});
  
  var PhotoCollection = Backbone.Collection.extend({
    model: PhotoModel,
    url: '/data/AppData.json',
    initialize: function() {
      this.fetch();
      return ;
      var col = this;
      navigator.geolocation.getCurrentPosition(
        function (position) {
          col.forEach(function(value) {
            var d = distance(position.coords.latitude,position.coords.longitude,value.LAT,value.LONG);
            var d_f;
            if(d<0){
              d_f = (d*1000).toFixed(1) + "m";
            }else{
              d_f = d.toFixed(2) + "km";
            }
            value.set('distance',d);
            value.set('distance_format',d_f);
         });
      }); 
    }
  });

  var photos = new PhotoCollection;

  var NavBackView = Backbone.View.extend({
    hide:function () {
      this.visible=false;
      this.$el.attr("hidden",true);
      this.nav.removeClass("active");
    },
    show:function () {
      this.visible=true;
      this.$el.removeAttr("hidden");
      this.nav.addClass("active");
    }
  });

  var MapView = NavBackView.extend({
    markers: [],
    el: $('#map'),
    nav: NavBar.find('li a[href="#map"]').parent(),
    map: null,
    mapOptions: {
      zoom: 10,
      center:  new google.maps.LatLng(36.1,-95.936),
      mapTypeControl: true,
      navigationControlOptions: {
        style: google.maps.NavigationControlStyle.ANDROID
      },
      mapTypeId: google.maps.MapTypeId.ROADMAP,
      streetViewControl: false,
    },
    
    initialize: function() {
      this.map = new google.maps.Map(document.getElementById('map-content'),this.mapOptions);

      photos.on("add",this.addMarker,this);
      photos.on("remove",this.removeMarker,this);
      photos.on("reset",function(){
        _.each(this.markers,function(marker){marker.setMap(null);});
        this.markers = [];
        photos.forEach(this.addMarker,this);
      },this);
    },
    removeMarker: function (model) {
      if(model.get("marker")){
        this.markers = _.without(this.markers,model.get("marker"));
        model.get("marker").setMap(null);
      }
    },
    addMarker: function (model) {
      var position = new google.maps.LatLng(model.get("LAT"),model.get("LONG"));
      var marker = new google.maps.Marker({
        position: position,
        map: this.map,
        title: model.get("NAME"),
        description: model.get("VISUAL_DESCRIPTION")
      });
      model.set("marker",marker);
      this.markers.push(marker);
    },
    render: function () {
      this.$('#map-content').height($(window).height() - $(".navbar").height());
    },
  });

  var NearYouView = NavBackView.extend({
    el: $('#nearyou'),
    nav: NavBar.find('li a[href="#near-you"]').parent(),
    template: Mustache.compile($('#listitem-template').text()),
    
    initialize: function () {
      var onUpdate = function() {
        if(this.visible)this.render();
      };
      photos.on("add",onUpdate,this);
      photos.on("remove",onUpdate,this);
      photos.on("reset",onUpdate,this);
      photos.on("change",onUpdate,this);
    },
    
    render: function () {
      this.$el.html(this.template({content:photos.toJSON()}));
    },
  });
  
  var SearchView = NavBackView.extend({
    el: $('#search'),
    content: $('#search #content'),
    nav: NavBar.find('li a[href="#search"]').parent(),
    template: Mustache.compile($('#listitem-template').text()),
    term: "",

    events: {
      "submit": "search",
    },
    initialize: function () {
      var onUpdate = function() {
        if(this.visible)this.render();
      };
      photos.on("add",onUpdate,this);
      photos.on("remove",onUpdate,this);
      photos.on("reset",onUpdate,this);
      photos.on("change",onUpdate,this);
    },
    render: function () {
      if(this.term){
        var e = decodeURI(this.term);
        this.$("input").val(e);
        var sd = photos.filter(function(i){
          return i.get("NAME").toLowerCase().indexOf(e) != -1 ||
             i.get("VISUAL_DESCRIPTION").toLowerCase().indexOf(e) != -1 ||
             i.get("ADDRESS").toLowerCase().indexOf(e) != -1 ||
             i.get("TAGS").toLowerCase().indexOf(e) != -1;
        });
        sd = _.map(sd,function(i){return i.toJSON();});
        this.content.html(this.template({content:sd}));
      } else {
        this.content.html("");
      }
    },
    search: function (en) {
      var e = encodeURI(this.$('input').val());
      this.term = e;
      this.render();
      navigationRouter.navigate("search/"+e,{trigger:false});
    }
  });
  
  var AboutView = NavBackView.extend({
    el: $('#about'),
    nav: NavBar.find('li a[href="#about"]').parent(),
    
    initialize: function () {
      this.$el.load('about.html #content');
    },
  });
  
  var NavigationRouter = Backbone.Router.extend({
    mapView: null,
    nearYouView: new NearYouView,
    searchView: new SearchView,
    aboutView: new AboutView,
    
    routes:{
      "": "map",
      "map": "map",
      "near-you": "nearYou",
      "search": "search",
      "search/": "search",
      "search/:term": "search",
      "about": "about",
    },
    initialize: function (options){
      
    },
    
    about: function () {
      if (this.currentView) this.currentView.hide()
      this.currentView = this.aboutView;
      this.aboutView.show();
      this.aboutView.render();
    },
    nearYou: function () {
      if (this.currentView) this.currentView.hide();
      this.currentView = this.nearYouView;
      this.nearYouView.show();
      this.nearYouView.render();
    },
    search: function (term) {
      if (this.currentView) this.currentView.hide();
      this.currentView = this.searchView;
      this.searchView.show();
      if (term) 
        this.searchView.term=decodeURI(term);
      else
        this.navigate("search/"+encodeURI(this.searchView.term),{trigger: false, replace: true});
      this.searchView.render();
    },
    map: function () {
      if (!this.mapView) this.mapView = new MapView();
      if (this.currentView) this.currentView.hide();
      this.currentView = this.mapView;
      this.mapView.show();
      this.mapView.render();
    }
  });
  
  var navigationRouter = new NavigationRouter;
  Backbone.history.start();
  
  // Helpers
  function distance(lat1,lon1,lat2,lon2) {
    var R = 6371; // km (change this constant to get miles)
    var dLat = (lat2-lat1) * Math.PI / 180;
    var dLon = (lon2-lon1) * Math.PI / 180;
    var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
      Math.cos(lat1 * Math.PI / 180 ) * Math.cos(lat2 * Math.PI / 180 ) *
      Math.sin(dLon/2) * Math.sin(dLon/2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    var d = R * c;
    return d;
  }
});
