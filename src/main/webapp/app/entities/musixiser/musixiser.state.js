(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('musixiser', {
            parent: 'entity',
            url: '/musixiser',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'musixiseApp.musixiser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/musixiser/musixisers.html',
                    controller: 'MusixiserController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('musixiser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('musixiser-detail', {
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
        .state('musixiser.new', {
            parent: 'musixiser',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/musixiser/musixiser-dialog.html',
                    controller: 'MusixiserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                realname: null,
                                tel: null,
                                email: null,
                                birth: null,
                                gender: null,
                                smallAvatar: null,
                                largeAvatar: null,
                                nation: null,
                                isMaster: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('musixiser', null, { reload: true });
                }, function() {
                    $state.go('musixiser');
                });
            }]
        })
        .state('musixiser.edit', {
            parent: 'musixiser',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/musixiser/musixiser-dialog.html',
                    controller: 'MusixiserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Musixiser', function(Musixiser) {
                            return Musixiser.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('musixiser', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('musixiser.delete', {
            parent: 'musixiser',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/musixiser/musixiser-delete-dialog.html',
                    controller: 'MusixiserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Musixiser', function(Musixiser) {
                            return Musixiser.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('musixiser', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
