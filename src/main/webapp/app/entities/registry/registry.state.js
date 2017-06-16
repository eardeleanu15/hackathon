(function() {
    'use strict';

    angular
        .module('appApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('registry', {
            parent: 'entity',
            url: '/registry',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Registries'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/registry/registries.html',
                    controller: 'RegistryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('registry-detail', {
            parent: 'registry',
            url: '/registry/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Registry'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/registry/registry-detail.html',
                    controller: 'RegistryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Registry', function($stateParams, Registry) {
                    return Registry.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'registry',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('registry.new', {
            parent: 'registry',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/registry/registry-dialog.html',
                    controller: 'RegistryDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function(){
                    return {
                        name: null,
                        description: null,
                        active: true,
                        archived: true,
                        registryFields: []
                    };
                },
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'registry',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('registry.edit', {
            parent: 'registry',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/registry/registry-dialog.html',
                    controller: 'RegistryDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Registry', function($stateParams, Registry) {
                    return Registry.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'registry',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('registry.delete', {
            parent: 'registry',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registry/registry-delete-dialog.html',
                    controller: 'RegistryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Registry', function(Registry) {
                            return Registry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('registry', null, { reload: 'registry' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
