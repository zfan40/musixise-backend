(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('AudienceDetailController', AudienceDetailController);

    AudienceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Audience'];

    function AudienceDetailController($scope, $rootScope, $stateParams, entity, Audience) {
        var vm = this;
        vm.audience = entity;
        vm.load = function (id) {
            Audience.get({id: id}, function(result) {
                vm.audience = result;
            });
        };
        var unsubscribe = $rootScope.$on('musixiseApp:audienceUpdate', function(event, result) {
            vm.audience = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
