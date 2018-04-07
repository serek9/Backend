(function() {
    'use strict';

    angular
        .module('serflixApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('movie-recomendation', {
            parent: 'entity',
            url: '/movie-recomendation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'serflixApp.movieRecomendation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/movie-recomendation/movie-recomendations.html',
                    controller: 'MovieRecomendationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('movieRecomendation');
                    $translatePartialLoader.addPart('recomendationResult');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('movie-recomendation-detail', {
            parent: 'entity',
            url: '/movie-recomendation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'serflixApp.movieRecomendation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/movie-recomendation/movie-recomendation-detail.html',
                    controller: 'MovieRecomendationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('movieRecomendation');
                    $translatePartialLoader.addPart('recomendationResult');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MovieRecomendation', function($stateParams, MovieRecomendation) {
                    return MovieRecomendation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'movie-recomendation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('movie-recomendation-detail.edit', {
            parent: 'movie-recomendation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/movie-recomendation/movie-recomendation-dialog.html',
                    controller: 'MovieRecomendationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MovieRecomendation', function(MovieRecomendation) {
                            return MovieRecomendation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('movie-recomendation.new', {
            parent: 'movie-recomendation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/movie-recomendation/movie-recomendation-dialog.html',
                    controller: 'MovieRecomendationDialogController',
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
                    $state.go('movie-recomendation', null, { reload: 'movie-recomendation' });
                }, function() {
                    $state.go('movie-recomendation');
                });
            }]
        })
        .state('movie-recomendation.edit', {
            parent: 'movie-recomendation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/movie-recomendation/movie-recomendation-dialog.html',
                    controller: 'MovieRecomendationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MovieRecomendation', function(MovieRecomendation) {
                            return MovieRecomendation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('movie-recomendation', null, { reload: 'movie-recomendation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('movie-recomendation.delete', {
            parent: 'movie-recomendation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/movie-recomendation/movie-recomendation-delete-dialog.html',
                    controller: 'MovieRecomendationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MovieRecomendation', function(MovieRecomendation) {
                            return MovieRecomendation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('movie-recomendation', null, { reload: 'movie-recomendation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
