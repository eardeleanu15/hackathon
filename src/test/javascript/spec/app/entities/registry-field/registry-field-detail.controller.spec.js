'use strict';

describe('Controller Tests', function() {

    describe('RegistryField Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRegistryField, MockRegistry, MockCaseFieldValue;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRegistryField = jasmine.createSpy('MockRegistryField');
            MockRegistry = jasmine.createSpy('MockRegistry');
            MockCaseFieldValue = jasmine.createSpy('MockCaseFieldValue');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'RegistryField': MockRegistryField,
                'Registry': MockRegistry,
                'CaseFieldValue': MockCaseFieldValue
            };
            createController = function() {
                $injector.get('$controller')("RegistryFieldDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'appApp:registryFieldUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
