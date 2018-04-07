(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('PreferencesDialogController', PreferencesDialogController);

    PreferencesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Preferences', 'User', 'MovieRecomendation', 'SerieRecomendation', 'Request'];

    function PreferencesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Preferences, User, MovieRecomendation, SerieRecomendation, Request) {
        var vm = this;

        vm.preferences = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.movierecomendations = MovieRecomendation.query();
        vm.serierecomendations = SerieRecomendation.query();
        vm.requests = Request.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.preferences.id !== null) {
                Preferences.update(vm.preferences, onSaveSuccess, onSaveError);
            } else {
                Preferences.save(vm.preferences, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('serflixApp:preferencesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
