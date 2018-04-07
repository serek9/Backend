(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('SerieRecomendationDialogController', SerieRecomendationDialogController);

    SerieRecomendationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SerieRecomendation', 'Serie', 'Request', 'Preferences'];

    function SerieRecomendationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SerieRecomendation, Serie, Request, Preferences) {
        var vm = this;

        vm.serieRecomendation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.series = Serie.query();
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
            if (vm.serieRecomendation.id !== null) {
                SerieRecomendation.update(vm.serieRecomendation, onSaveSuccess, onSaveError);
            } else {
                SerieRecomendation.save(vm.serieRecomendation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('serflixApp:serieRecomendationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
