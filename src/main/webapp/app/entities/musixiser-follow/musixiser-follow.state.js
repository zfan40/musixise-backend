(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('musixiser-follow', {
            parent: 'entity',
            url: '/musixiser-follow?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.musixiserFollow.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/musixiser-follow/musixiser-follows.html',
                    controller: 'MusixiserFollowController',
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
                    $translatePartialLoader.addPart('musixiserFollow');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('musixiser-follow-detail', {
            parent: 'entity',
            url: '/musixiser-follow/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.musixiserFollow.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/musixiser-follow/musixiser-follow-detail.html',
                    controller: 'MusixiserFollowDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('musixiserFollow');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MusixiserFollow', function($stateParams, MusixiserFollow) {
                    return MusixiserFollow.get({id : $stateParams.id});
                }]
            }
        })
        .state('musixiser-follow.new', {
            parent: 'musixiser-follow',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/musixiser-follow/musixiser-follow-dialog.html',
                    controller: 'MusixiserFollowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                followUid: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('musixiser-follow', null, { reload: true });
                }, function() {
                    $state.go('musixiser-follow');
                });
            }]
        })
        .state('musixiser-follow.edit', {
            parent: 'musixiser-follow',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/musixiser-follow/musixiser-follow-dialog.html',
                    controller: 'MusixiserFollowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MusixiserFollow', function(MusixiserFollow) {
                            return MusixiserFollow.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('musixiser-follow', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('musixiser-follow.delete', {
            parent: 'musixiser-follow',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/musixiser-follow/musixiser-follow-delete-dialog.html',
                    controller: 'MusixiserFollowDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MusixiserFollow', function(MusixiserFollow) {
                            return MusixiserFollow.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('musixiser-follow', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
