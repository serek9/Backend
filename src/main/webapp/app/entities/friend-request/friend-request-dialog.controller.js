(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('FriendRequestDialogController', FriendRequestDialogController);

    FriendRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FriendRequest', 'User'];

    function FriendRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FriendRequest, User) {
        var vm = this;

        vm.friendRequest = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.friendRequest.id !== null) {
                FriendRequest.update(vm.friendRequest, onSaveSuccess, onSaveError);
            } else {
                FriendRequest.save(vm.friendRequest, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('serflixApp:friendRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.sentOn = false;
        vm.datePickerOpenStatus.resolvedOn = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
