(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('SerieDialogController', SerieDialogController);

    SerieDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Serie', 'SerieRecomendation'];

    function SerieDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Serie, SerieRecomendation) {
        var vm = this;

        vm.serie = entity;
        vm.clear = clear;
        vm.save = save;
        vm.serierecomendations = SerieRecomendation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serie.id !== null) {
                Serie.update(vm.serie, onSaveSuccess, onSaveError);
            } else {
                Serie.save(vm.serie, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('serflixApp:serieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
