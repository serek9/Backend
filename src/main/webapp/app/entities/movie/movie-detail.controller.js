(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('MovieDetailController', MovieDetailController);

    MovieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Movie', 'MovieRecomendation'];

    function MovieDetailController($scope, $rootScope, $stateParams, previousState, entity, Movie, MovieRecomendation) {
        var vm = this;

        vm.movie = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('serflixApp:movieUpdate', function(event, result) {
            vm.movie = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
