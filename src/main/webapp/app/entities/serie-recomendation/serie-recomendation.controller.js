(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('SerieRecomendationController', SerieRecomendationController);

    SerieRecomendationController.$inject = ['$scope', '$state', 'SerieRecomendation'];

    function SerieRecomendationController ($scope, $state, SerieRecomendation) {
        var vm = this;

        vm.serieRecomendations = [];

        loadAll();

        function loadAll() {
            SerieRecomendation.query(function(result) {
                vm.serieRecomendations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
