(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('audience', {
            parent: 'entity',
            url: '/audience?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.audience.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/audience/audiences.html',
                    controller: 'AudienceController',
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
                    $translatePartialLoader.addPart('audience');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('audience-detail', {
            parent: 'entity',
            url: '/audience/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.audience.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/audience/audience-detail.html',
                    controller: 'AudienceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('audience');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Audience', function($stateParams, Audience) {
                    return Audience.get({id : $stateParams.id});
                }]
            }
        })
        .state('audience.new', {
            parent: 'audience',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audience/audience-dialog.html',
                    controller: 'AudienceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nickname: null,
                                realname: null,
                                email: null,
                                tel: null,
                                userId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('audience', null, { reload: true });
                }, function() {
                    $state.go('audience');
                });
            }]
        })
        .state('audience.edit', {
            parent: 'audience',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audience/audience-dialog.html',
                    controller: 'AudienceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Audience', function(Audience) {
                            return Audience.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('audience', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('audience.delete', {
            parent: 'audience',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audience/audience-delete-dialog.html',
                    controller: 'AudienceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Audience', function(Audience) {
                            return Audience.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('audience', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
