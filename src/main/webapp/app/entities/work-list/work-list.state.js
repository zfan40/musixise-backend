(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('work-list', {
            parent: 'entity',
            url: '/work-list?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.workList.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-list/work-lists.html',
                    controller: 'WorkListController',
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
                    $translatePartialLoader.addPart('workList');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('work-list-detail', {
            parent: 'entity',
            url: '/work-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.workList.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-list/work-list-detail.html',
                    controller: 'WorkListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('workList');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WorkList', function($stateParams, WorkList) {
                    return WorkList.get({id : $stateParams.id});
                }]
            }
        })
        .state('work-list.new', {
            parent: 'work-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-list/work-list-dialog.html',
                    controller: 'WorkListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                content: null,
                                url: null,
                                createtime: null,
                                userId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('work-list', null, { reload: true });
                }, function() {
                    $state.go('work-list');
                });
            }]
        })
        .state('work-list.edit', {
            parent: 'work-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-list/work-list-dialog.html',
                    controller: 'WorkListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WorkList', function(WorkList) {
                            return WorkList.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-list', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('work-list.delete', {
            parent: 'work-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-list/work-list-delete-dialog.html',
                    controller: 'WorkListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WorkList', function(WorkList) {
                            return WorkList.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-list', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
