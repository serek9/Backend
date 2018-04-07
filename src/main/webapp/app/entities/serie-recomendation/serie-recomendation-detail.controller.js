(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('SerieRecomendationDetailController', SerieRecomendationDetailController);

    SerieRecomendationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SerieRecomendation', 'Serie', 'Request', 'Preferences'];

    function SerieRecomendationDetailController($scope, $rootScope, $stateParams, previousState, entity, SerieRecomendation, Serie, Request, Preferences) {
        var vm = this;

        vm.serieRecomendation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('serflixApp:serieRecomendationUpdate', function(event, result) {
            vm.serieRecomendation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
