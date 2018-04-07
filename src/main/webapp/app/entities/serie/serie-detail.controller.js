(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('SerieDetailController', SerieDetailController);

    SerieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Serie', 'SerieRecomendation'];

    function SerieDetailController($scope, $rootScope, $stateParams, previousState, entity, Serie, SerieRecomendation) {
        var vm = this;

        vm.serie = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('serflixApp:serieUpdate', function(event, result) {
            vm.serie = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
