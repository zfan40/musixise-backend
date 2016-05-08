(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('work-list-follow', {
            parent: 'entity',
            url: '/work-list-follow',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.workListFollow.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-list-follow/work-list-follows.html',
                    controller: 'WorkListFollowController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('workListFollow');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('work-list-follow-detail', {
            parent: 'entity',
            url: '/work-list-follow/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.workListFollow.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-list-follow/work-list-follow-detail.html',
                    controller: 'WorkListFollowDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('workListFollow');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WorkListFollow', function($stateParams, WorkListFollow) {
                    return WorkListFollow.get({id : $stateParams.id});
                }]
            }
        })
        .state('work-list-follow.new', {
            parent: 'work-list-follow',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-list-follow/work-list-follow-dialog.html',
                    controller: 'WorkListFollowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createtime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('work-list-follow', null, { reload: true });
                }, function() {
                    $state.go('work-list-follow');
                });
            }]
        })
        .state('work-list-follow.edit', {
            parent: 'work-list-follow',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-list-follow/work-list-follow-dialog.html',
                    controller: 'WorkListFollowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WorkListFollow', function(WorkListFollow) {
                            return WorkListFollow.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-list-follow', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('work-list-follow.delete', {
            parent: 'work-list-follow',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-list-follow/work-list-follow-delete-dialog.html',
                    controller: 'WorkListFollowDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WorkListFollow', function(WorkListFollow) {
                            return WorkListFollow.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-list-follow', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
