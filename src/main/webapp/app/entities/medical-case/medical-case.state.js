(function() {
    'use strict';

    angular
        .module('appApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('medical-case', {
            parent: 'entity',
            url: '/medical-case',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MedicalCases'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medical-case/medical-cases.html',
                    controller: 'MedicalCaseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('medical-case-detail', {
            parent: 'medical-case',
            url: '/medical-case/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MedicalCase'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medical-case/medical-case-detail.html',
                    controller: 'MedicalCaseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MedicalCase', function($stateParams, MedicalCase) {
                    return MedicalCase.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'medical-case',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('medical-case-detail.edit', {
            parent: 'medical-case-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case/medical-case-dialog.html',
                    controller: 'MedicalCaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MedicalCase', function(MedicalCase) {
                            return MedicalCase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medical-case.new', {
            parent: 'medical-case',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case/medical-case-dialog.html',
                    controller: 'MedicalCaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                approvalStatus: null,
                                createdon: null,
                                archived: null,
                                version: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('medical-case', null, { reload: 'medical-case' });
                }, function() {
                    $state.go('medical-case');
                });
            }]
        })
        .state('medical-case.new-registry', {
            parent: 'medical-case',
            url: '/new-registry/{id}',
            data: {
                authorities: ['ROLE_USER'],
            },
            onEnter: ['$stateParams', '$state', '$uibModal', 'Registry', function($stateParams, $state, $uibModal, Registry) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case/medical-case-dialog.html',
                    controller: 'MedicalCaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function() {
                            return {
                                approvalStatus: null,
                                createdon: null,
                                archived: null,
                                version: null,
                                id: null,
                                registry: Registry.get({id : $stateParams.id})
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('medical-case', null, { reload: 'medical-case' });
                }, function() {
                    $state.go('medical-case');
                });
            }]
        })
        // .state('medical-case.new-registry', {
        //     parent: 'medical-case',
        //     url: '/new-registry/{id}',
        //     data: {
        //         authorities: ['ROLE_USER'],
        //     },
        //     views: {
        //         'content@': {
        //             templateUrl: 'app/entities/medical-case/medical-case-dialog.html',
        //             controller: 'MedicalCaseDialogController',
        //             controllerAs: 'vm'
        //         }
        //     },
        //     resolve: {
        //         entity: function() {
        //             console.log('>>>>>>>>>');
        //             return {
        //                 approvalStatus: null,
        //                 createdon: null,
        //                 archived: null,
        //                 version: null,
        //                 id: null,
        //                 registry: Registry.get({id : $stateParams.id})
        //             };
        //         }
        //     }
        // })
        .state('medical-case.edit', {
            parent: 'medical-case',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case/medical-case-dialog.html',
                    controller: 'MedicalCaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MedicalCase', function(MedicalCase) {
                            return MedicalCase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medical-case', null, { reload: 'medical-case' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medical-case.delete', {
            parent: 'medical-case',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-case/medical-case-delete-dialog.html',
                    controller: 'MedicalCaseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MedicalCase', function(MedicalCase) {
                            return MedicalCase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medical-case', null, { reload: 'medical-case' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
