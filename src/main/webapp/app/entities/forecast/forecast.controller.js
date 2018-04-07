(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('ForecastController', ForecastController);

    ForecastController.$inject = ['$scope', '$state', 'Forecast'];

    function ForecastController ($scope, $state, Forecast) {
        var vm = this;

        vm.forecasts = [];

        loadAll();

        function loadAll() {
            Forecast.query(function(result) {
                vm.forecasts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
