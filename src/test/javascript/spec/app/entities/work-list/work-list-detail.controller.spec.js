'use strict';

describe('Controller Tests', function() {

    describe('WorkList Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWorkList;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWorkList = jasmine.createSpy('MockWorkList');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'WorkList': MockWorkList
            };
            createController = function() {
                $injector.get('$controller')("WorkListDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'musixiseApp:workListUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
