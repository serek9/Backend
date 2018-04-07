(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('PreferencesDetailController', PreferencesDetailController);

    PreferencesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Preferences', 'User', 'MovieRecomendation', 'SerieRecomendation', 'Request'];

    function PreferencesDetailController($scope, $rootScope, $stateParams, previousState, entity, Preferences, User, MovieRecomendation, SerieRecomendation, Request) {
        var vm = this;

        vm.preferences = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('serflixApp:preferencesUpdate', function(event, result) {
            vm.preferences = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
