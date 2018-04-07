(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('ForecastDetailController', ForecastDetailController);

    ForecastDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Forecast', 'Request'];

    function ForecastDetailController($scope, $rootScope, $stateParams, previousState, entity, Forecast, Request) {
        var vm = this;

        vm.forecast = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('serflixApp:forecastUpdate', function(event, result) {
            vm.forecast = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
