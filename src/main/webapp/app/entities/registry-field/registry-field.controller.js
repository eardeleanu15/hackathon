(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('RegistryFieldController', RegistryFieldController);

    RegistryFieldController.$inject = ['RegistryField'];

    function RegistryFieldController(RegistryField) {

        var vm = this;

        vm.registryFields = [];

        loadAll();

        function loadAll() {
            RegistryField.query(function(result) {
                vm.registryFields = result;
                vm.searchQuery = null;
            });
        }
    }
})();
