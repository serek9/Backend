(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('SerieRecomendationDeleteController',SerieRecomendationDeleteController);

    SerieRecomendationDeleteController.$inject = ['$uibModalInstance', 'entity', 'SerieRecomendation'];

    function SerieRecomendationDeleteController($uibModalInstance, entity, SerieRecomendation) {
        var vm = this;

        vm.serieRecomendation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SerieRecomendation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
