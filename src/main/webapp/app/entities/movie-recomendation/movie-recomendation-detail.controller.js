(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('MovieRecomendationDetailController', MovieRecomendationDetailController);

    MovieRecomendationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MovieRecomendation', 'Movie', 'Request', 'Preferences'];

    function MovieRecomendationDetailController($scope, $rootScope, $stateParams, previousState, entity, MovieRecomendation, Movie, Request, Preferences) {
        var vm = this;

        vm.movieRecomendation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('serflixApp:movieRecomendationUpdate', function(event, result) {
            vm.movieRecomendation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
