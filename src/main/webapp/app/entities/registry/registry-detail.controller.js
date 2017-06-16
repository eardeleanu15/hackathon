(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('RegistryDetailController', RegistryDetailController);

    RegistryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Registry', 'RegistryField', 'MedicalCase'];

    function RegistryDetailController($scope, $rootScope, $stateParams, previousState, entity, Registry, RegistryField, MedicalCase) {
        var vm = this;

        vm.registry = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('appApp:registryUpdate', function(event, result) {
            vm.registry = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
