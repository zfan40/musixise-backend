(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stages-follow', {
            parent: 'entity',
            url: '/stages-follow?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.stagesFollow.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stages-follow/stages-follows.html',
                    controller: 'StagesFollowController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stagesFollow');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stages-follow-detail', {
            parent: 'entity',
            url: '/stages-follow/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.stagesFollow.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stages-follow/stages-follow-detail.html',
                    controller: 'StagesFollowDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stagesFollow');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StagesFollow', function($stateParams, StagesFollow) {
                    return StagesFollow.get({id : $stateParams.id});
                }]
            }
        })
        .state('stages-follow.new', {
            parent: 'stages-follow',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stages-follow/stages-follow-dialog.html',
                    controller: 'StagesFollowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                musixiserUid: null,
                                audienceUid: null,
                                stagesId: null,
                                timestamp: null,
                                updatetime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stages-follow', null, { reload: true });
                }, function() {
                    $state.go('stages-follow');
                });
            }]
        })
        .state('stages-follow.edit', {
            parent: 'stages-follow',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stages-follow/stages-follow-dialog.html',
                    controller: 'StagesFollowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StagesFollow', function(StagesFollow) {
                            return StagesFollow.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('stages-follow', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stages-follow.delete', {
            parent: 'stages-follow',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stages-follow/stages-follow-delete-dialog.html',
                    controller: 'StagesFollowDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StagesFollow', function(StagesFollow) {
                            return StagesFollow.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('stages-follow', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
