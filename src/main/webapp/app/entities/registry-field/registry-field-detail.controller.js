(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('RegistryFieldDetailController', RegistryFieldDetailController);

    RegistryFieldDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RegistryField', 'Registry', 'CaseFieldValue'];

    function RegistryFieldDetailController($scope, $rootScope, $stateParams, previousState, entity, RegistryField, Registry, CaseFieldValue) {
        var vm = this;

        vm.registryField = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('appApp:registryFieldUpdate', function(event, result) {
            vm.registryField = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
