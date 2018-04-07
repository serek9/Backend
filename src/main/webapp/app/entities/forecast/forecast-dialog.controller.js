(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('ForecastDialogController', ForecastDialogController);

    ForecastDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Forecast', 'Request'];

    function ForecastDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Forecast, Request) {
        var vm = this;

        vm.forecast = entity;
        vm.clear = clear;
        vm.save = save;
        vm.requests = Request.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.forecast.id !== null) {
                Forecast.update(vm.forecast, onSaveSuccess, onSaveError);
            } else {
                Forecast.save(vm.forecast, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('serflixApp:forecastUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
