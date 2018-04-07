(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('ForecastDeleteController',ForecastDeleteController);

    ForecastDeleteController.$inject = ['$uibModalInstance', 'entity', 'Forecast'];

    function ForecastDeleteController($uibModalInstance, entity, Forecast) {
        var vm = this;

        vm.forecast = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Forecast.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
