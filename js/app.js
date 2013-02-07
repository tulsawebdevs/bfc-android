Zepto(function($){
  
  var NavBar = $(".nav");
  
  var PhotoModel = Backbone.Model.extend({
    calc_distance: function (lat,lng){
      return distance(this.get("LAT"),this.get("LONG"),lat,lng);
    },
    calc_distance_format: function(lat,lng){
      var d = this.calc_distance(lat,lng);
      if(d<0){
        return (d*1000).toFixed(1) + "m";
      }else {
        return d.toFixed(2) + "km";
      }
    },
    update_distance: function(lat,lng){
      //this.set("distance",this.calc_distance(lat,lng));
      this.set('distance_format',this.calc_distance_format(lat,lng));
    }
  });
  
  var PhotoCollection = Backbone.Collection.extend({
    model: PhotoModel,
    url: 'data/AppData.json',
    initialize: function() {
      this.fetch();      
      var col = this;
      navigator.geolocation.getCurrentPosition(
        function (position) {
          col.location = position;
          col.invoke("update_distance",position.coords.latitude,position.coords.longitude);
      }); 
    }
  });

  var photos = new PhotoCollection;

  var ListItemView = Backbone.View.extend({
    template: Mustache.compile($('#listitem-template').text()),
    initialize: function() {
    },
    render: function() {
      
    }
  });

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

  var ListView = NavBackView.extend({
    data: [],    
    initialize: function () {
      var _this = this;
      var onUpdate = function() {
        if(_this.visible){
          _this.update();
          _this.render();
        }
      };
      photos.on("add",onUpdate);
      photos.on("remove",onUpdate);
      photos.on("reset",onUpdate);
      photos.on("change",onUpdate);
    },
    update :function () {},
    render: function () {
      var d = this.data.toJSON ? this.data.toJSON() : this.data;
      this.content.html(this.template({content:d}));
    },
  });

  var NearYouView = ListView.extend({
    el: $('#nearyou'),
    content: $('#nearyou'),
    nav: NavBar.find('li a[href="#near-you"]').parent(),
    template: Mustache.compile($('#listitem-template').text()),
    data: photos.toJSON(),
    
    update: function (){
      this.data = photos.toJSON();
      this.render();
    },    
  });
  
  var SearchView = ListView.extend({
    el: $('#search'),
    content: $('#search #content'),
    nav: NavBar.find('li a[href="#search"]').parent(),
    template: Mustache.compile($('#listitem-template').text()),
    term: "",

    events: {
      "submit": "search",
    },
    update: function() {
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
        this.data = sd;
      } else {
        this.data = [];
      }
      this.render();
    },
    search: function (en) {
      var e = encodeURI(this.$('input').val());
      this.term = e;
      this.update();
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
