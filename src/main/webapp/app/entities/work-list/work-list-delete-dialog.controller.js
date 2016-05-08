(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('WorkListDeleteController',WorkListDeleteController);

    WorkListDeleteController.$inject = ['$uibModalInstance', 'entity', 'WorkList'];

    function WorkListDeleteController($uibModalInstance, entity, WorkList) {
        var vm = this;
        vm.workList = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            WorkList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
