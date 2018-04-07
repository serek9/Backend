(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('SerieController', SerieController);

    SerieController.$inject = ['$scope', '$state', 'Serie'];

    function SerieController ($scope, $state, Serie) {
        var vm = this;

        vm.series = [];

        loadAll();

        function loadAll() {
            Serie.query(function(result) {
                vm.series = result;
                vm.searchQuery = null;
            });
        }
    }
})();
