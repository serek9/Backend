(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('MovieRecomendationDeleteController',MovieRecomendationDeleteController);

    MovieRecomendationDeleteController.$inject = ['$uibModalInstance', 'entity', 'MovieRecomendation'];

    function MovieRecomendationDeleteController($uibModalInstance, entity, MovieRecomendation) {
        var vm = this;

        vm.movieRecomendation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MovieRecomendation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
