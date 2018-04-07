(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('MovieRecomendationController', MovieRecomendationController);

    MovieRecomendationController.$inject = ['$scope', '$state', 'MovieRecomendation'];

    function MovieRecomendationController ($scope, $state, MovieRecomendation) {
        var vm = this;

        vm.movieRecomendations = [];

        loadAll();

        function loadAll() {
            MovieRecomendation.query(function(result) {
                vm.movieRecomendations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
