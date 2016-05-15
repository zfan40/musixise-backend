(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stages', {
            parent: 'entity',
            url: '/stages?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.stages.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stages/stages.html',
                    controller: 'StagesController',
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
                    $translatePartialLoader.addPart('stages');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stages-detail', {
            parent: 'entity',
            url: '/stages/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.stages.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stages/stages-detail.html',
                    controller: 'StagesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stages');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Stages', function($stateParams, Stages) {
                    return Stages.get({id : $stateParams.id});
                }]
            }
        })
        .state('stages.new', {
            parent: 'stages',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stages/stages-dialog.html',
                    controller: 'StagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                createtime: null,
                                userId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stages', null, { reload: true });
                }, function() {
                    $state.go('stages');
                });
            }]
        })
        .state('stages.edit', {
            parent: 'stages',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stages/stages-dialog.html',
                    controller: 'StagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Stages', function(Stages) {
                            return Stages.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('stages', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stages.delete', {
            parent: 'stages',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stages/stages-delete-dialog.html',
                    controller: 'StagesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Stages', function(Stages) {
                            return Stages.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('stages', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
