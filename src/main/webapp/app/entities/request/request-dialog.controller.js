(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('RequestDialogController', RequestDialogController);

    RequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Request', 'User', 'Location', 'Forecast', 'MovieRecomendation', 'SerieRecomendation', 'Preferences'];

    function RequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Request, User, Location, Forecast, MovieRecomendation, SerieRecomendation, Preferences) {
        var vm = this;

        vm.request = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.locations = Location.query();
        vm.forecasts = Forecast.query();
        vm.movierecomendations = MovieRecomendation.query();
        vm.serierecomendations = SerieRecomendation.query();
        vm.preferences = Preferences.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.request.id !== null) {
                Request.update(vm.request, onSaveSuccess, onSaveError);
            } else {
                Request.save(vm.request, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('serflixApp:requestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.viewDate = false;
        vm.datePickerOpenStatus.creationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
