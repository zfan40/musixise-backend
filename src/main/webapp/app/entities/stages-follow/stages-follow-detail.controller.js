(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('StagesFollowDetailController', StagesFollowDetailController);

    StagesFollowDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'StagesFollow'];

    function StagesFollowDetailController($scope, $rootScope, $stateParams, entity, StagesFollow) {
        var vm = this;
        vm.stagesFollow = entity;
        vm.load = function (id) {
            StagesFollow.get({id: id}, function(result) {
                vm.stagesFollow = result;
            });
        };
        var unsubscribe = $rootScope.$on('musixiseApp:stagesFollowUpdate', function(event, result) {
            vm.stagesFollow = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
