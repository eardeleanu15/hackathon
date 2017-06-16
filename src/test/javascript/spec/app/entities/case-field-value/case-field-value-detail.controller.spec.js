'use strict';

describe('Controller Tests', function() {

    describe('CaseFieldValue Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCaseFieldValue, MockRegistryField, MockMedicalCase;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCaseFieldValue = jasmine.createSpy('MockCaseFieldValue');
            MockRegistryField = jasmine.createSpy('MockRegistryField');
            MockMedicalCase = jasmine.createSpy('MockMedicalCase');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CaseFieldValue': MockCaseFieldValue,
                'RegistryField': MockRegistryField,
                'MedicalCase': MockMedicalCase
            };
            createController = function() {
                $injector.get('$controller')("CaseFieldValueDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'appApp:caseFieldValueUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
