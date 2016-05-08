(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('WorkListFollowDeleteController',WorkListFollowDeleteController);

    WorkListFollowDeleteController.$inject = ['$uibModalInstance', 'entity', 'WorkListFollow'];

    function WorkListFollowDeleteController($uibModalInstance, entity, WorkListFollow) {
        var vm = this;
        vm.workListFollow = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            WorkListFollow.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
