(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('config', {
                parent: 'entity',
                url: '/config?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '配置管理'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/config/configs.html',
                        controller: 'ConfigController',
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
                        //$translatePartialLoader.addPart('config');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('config-detail', {
                parent: 'entity',
                url: '/musixiser/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'musixiseApp.musixiser.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/musixiser/musixiser-detail.html',
                        controller: 'MusixiserDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('musixiser');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Musixiser', function($stateParams, Musixiser) {
                        return Musixiser.get({id : $stateParams.id});
                    }]
                }
            })
            .state('config.new', {
                parent: 'config',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/config/config-dialog.html',
                        controller: 'ConfigDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    ckey: null,
                                    cval: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('config', null, { reload: true });
                    }, function() {
                        $state.go('config');
                    });
                }]
            })
            .state('config.edit', {
                parent: 'config',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/config/config-dialog.html',
                        controller: 'ConfigDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Config', function(Config) {
                                return Config.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('config', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('config.delete', {
                parent: 'config',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/config/config-delete-dialog.html',
                        controller: 'ConfigDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Config', function(Config) {
                                return Config.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('config', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    }

})();
