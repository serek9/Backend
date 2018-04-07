(function() {
    'use strict';

    angular
        .module('serflixApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('serie-recomendation', {
            parent: 'entity',
            url: '/serie-recomendation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'serflixApp.serieRecomendation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/serie-recomendation/serie-recomendations.html',
                    controller: 'SerieRecomendationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serieRecomendation');
                    $translatePartialLoader.addPart('recomendationResult');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('serie-recomendation-detail', {
            parent: 'entity',
            url: '/serie-recomendation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'serflixApp.serieRecomendation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/serie-recomendation/serie-recomendation-detail.html',
                    controller: 'SerieRecomendationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serieRecomendation');
                    $translatePartialLoader.addPart('recomendationResult');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SerieRecomendation', function($stateParams, SerieRecomendation) {
                    return SerieRecomendation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'serie-recomendation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('serie-recomendation-detail.edit', {
            parent: 'serie-recomendation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serie-recomendation/serie-recomendation-dialog.html',
                    controller: 'SerieRecomendationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SerieRecomendation', function(SerieRecomendation) {
                            return SerieRecomendation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('serie-recomendation.new', {
            parent: 'serie-recomendation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serie-recomendation/serie-recomendation-dialog.html',
                    controller: 'SerieRecomendationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                recomendationResult: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('serie-recomendation', null, { reload: 'serie-recomendation' });
                }, function() {
                    $state.go('serie-recomendation');
                });
            }]
        })
        .state('serie-recomendation.edit', {
            parent: 'serie-recomendation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serie-recomendation/serie-recomendation-dialog.html',
                    controller: 'SerieRecomendationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SerieRecomendation', function(SerieRecomendation) {
                            return SerieRecomendation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('serie-recomendation', null, { reload: 'serie-recomendation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('serie-recomendation.delete', {
            parent: 'serie-recomendation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serie-recomendation/serie-recomendation-delete-dialog.html',
                    controller: 'SerieRecomendationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SerieRecomendation', function(SerieRecomendation) {
                            return SerieRecomendation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('serie-recomendation', null, { reload: 'serie-recomendation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
