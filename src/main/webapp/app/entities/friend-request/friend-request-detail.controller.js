(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('FriendRequestDetailController', FriendRequestDetailController);

    FriendRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FriendRequest', 'User'];

    function FriendRequestDetailController($scope, $rootScope, $stateParams, previousState, entity, FriendRequest, User) {
        var vm = this;

        vm.friendRequest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('serflixApp:friendRequestUpdate', function(event, result) {
            vm.friendRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
