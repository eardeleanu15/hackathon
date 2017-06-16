(function() {
    'use strict';

    angular
        .module('appApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('registry-field', {
            parent: 'entity',
            url: '/registry-field',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RegistryFields'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/registry-field/registry-fields.html',
                    controller: 'RegistryFieldController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('registry-field-detail', {
            parent: 'registry-field',
            url: '/registry-field/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RegistryField'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/registry-field/registry-field-detail.html',
                    controller: 'RegistryFieldDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'RegistryField', function($stateParams, RegistryField) {
                    return RegistryField.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'registry-field',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('registry-field-detail.edit', {
            parent: 'registry-field-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registry-field/registry-field-dialog.html',
                    controller: 'RegistryFieldDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RegistryField', function(RegistryField) {
                            return RegistryField.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('registry-field.new', {
            parent: 'registry-field',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registry-field/registry-field-dialog.html',
                    controller: 'RegistryFieldDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                active: null,
                                mandatory: null,
                                type: null,
                                maxLength: null,
                                values: null,
                                defaultValue: null,
                                group: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('registry-field', null, { reload: 'registry-field' });
                }, function() {
                    $state.go('registry-field');
                });
            }]
        })
        .state('registry-field.edit', {
            parent: 'registry-field',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registry-field/registry-field-dialog.html',
                    controller: 'RegistryFieldDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RegistryField', function(RegistryField) {
                            return RegistryField.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('registry-field', null, { reload: 'registry-field' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('registry-field.delete', {
            parent: 'registry-field',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/registry-field/registry-field-delete-dialog.html',
                    controller: 'RegistryFieldDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RegistryField', function(RegistryField) {
                            return RegistryField.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('registry-field', null, { reload: 'registry-field' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
