(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('MovieRecomendationDialogController', MovieRecomendationDialogController);

    MovieRecomendationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MovieRecomendation', 'Movie', 'Request', 'Preferences'];

    function MovieRecomendationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MovieRecomendation, Movie, Request, Preferences) {
        var vm = this;

        vm.movieRecomendation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.movies = Movie.query();
        vm.requests = Request.query();
        vm.preferences = Preferences.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.movieRecomendation.id !== null) {
                MovieRecomendation.update(vm.movieRecomendation, onSaveSuccess, onSaveError);
            } else {
                MovieRecomendation.save(vm.movieRecomendation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('serflixApp:movieRecomendationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
