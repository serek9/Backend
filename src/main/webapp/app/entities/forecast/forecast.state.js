(function() {
    'use strict';

    angular
        .module('serflixApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('forecast', {
            parent: 'entity',
            url: '/forecast',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'serflixApp.forecast.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/forecast/forecasts.html',
                    controller: 'ForecastController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('forecast');
                    $translatePartialLoader.addPart('weather');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('forecast-detail', {
            parent: 'entity',
            url: '/forecast/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'serflixApp.forecast.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/forecast/forecast-detail.html',
                    controller: 'ForecastDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('forecast');
                    $translatePartialLoader.addPart('weather');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Forecast', function($stateParams, Forecast) {
                    return Forecast.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'forecast',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('forecast-detail.edit', {
            parent: 'forecast-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forecast/forecast-dialog.html',
                    controller: 'ForecastDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Forecast', function(Forecast) {
                            return Forecast.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('forecast.new', {
            parent: 'forecast',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forecast/forecast-dialog.html',
                    controller: 'ForecastDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                temperature: null,
                                weather: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('forecast', null, { reload: 'forecast' });
                }, function() {
                    $state.go('forecast');
                });
            }]
        })
        .state('forecast.edit', {
            parent: 'forecast',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forecast/forecast-dialog.html',
                    controller: 'ForecastDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Forecast', function(Forecast) {
                            return Forecast.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('forecast', null, { reload: 'forecast' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('forecast.delete', {
            parent: 'forecast',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forecast/forecast-delete-dialog.html',
                    controller: 'ForecastDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Forecast', function(Forecast) {
                            return Forecast.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('forecast', null, { reload: 'forecast' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
