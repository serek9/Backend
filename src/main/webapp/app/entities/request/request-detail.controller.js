(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('RequestDetailController', RequestDetailController);

    RequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Request', 'User', 'Location', 'Forecast', 'MovieRecomendation', 'SerieRecomendation', 'Preferences'];

    function RequestDetailController($scope, $rootScope, $stateParams, previousState, entity, Request, User, Location, Forecast, MovieRecomendation, SerieRecomendation, Preferences) {
        var vm = this;

        vm.request = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('serflixApp:requestUpdate', function(event, result) {
            vm.request = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
