(function() {
    'use strict';

    angular
        .module('appApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('case-field-value', {
            parent: 'entity',
            url: '/case-field-value',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CaseFieldValues'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/case-field-value/case-field-values.html',
                    controller: 'CaseFieldValueController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('case-field-value-detail', {
            parent: 'case-field-value',
            url: '/case-field-value/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CaseFieldValue'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/case-field-value/case-field-value-detail.html',
                    controller: 'CaseFieldValueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'CaseFieldValue', function($stateParams, CaseFieldValue) {
                    return CaseFieldValue.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'case-field-value',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('case-field-value-detail.edit', {
            parent: 'case-field-value-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/case-field-value/case-field-value-dialog.html',
                    controller: 'CaseFieldValueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CaseFieldValue', function(CaseFieldValue) {
                            return CaseFieldValue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('case-field-value.new', {
            parent: 'case-field-value',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/case-field-value/case-field-value-dialog.html',
                    controller: 'CaseFieldValueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                value: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('case-field-value', null, { reload: 'case-field-value' });
                }, function() {
                    $state.go('case-field-value');
                });
            }]
        })
        .state('case-field-value.edit', {
            parent: 'case-field-value',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/case-field-value/case-field-value-dialog.html',
                    controller: 'CaseFieldValueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CaseFieldValue', function(CaseFieldValue) {
                            return CaseFieldValue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('case-field-value', null, { reload: 'case-field-value' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('case-field-value.delete', {
            parent: 'case-field-value',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/case-field-value/case-field-value-delete-dialog.html',
                    controller: 'CaseFieldValueDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CaseFieldValue', function(CaseFieldValue) {
                            return CaseFieldValue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('case-field-value', null, { reload: 'case-field-value' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
